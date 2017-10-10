#!/bin/bash
clear



LIB_VERSIONS_PATH="java/resources/lib-versions"



############################################################



echo
echo "Downloading library files.."
echo
php download-ftdi-libraries.php || exit 1



echo
echo "Copying library files.."
echo



\pushd ../ >/dev/null || exit 1
function CopyLibs() {
	CWD=`\pwd`
	osName="$1"
	# read lib version number
	if [ -z $LIB_VERSIONS_PATH ]; then
		echo "LIB_VERSIONS_PATH not set!"
		exit 1
	fi
	libVersion=`\cat $LIB_VERSIONS_PATH/${osName}-version.txt`
	if [ -z $libVersion ]; then
		echo "Failed to get lib version for os: $osName"
		exit 1
	fi
	# prepare paths
	filesPath="${2/<version>/$libVersion}"
	filesFormat="${3/<version>/$libVersion}"
	renameFile="$4"
	echo
	echo " libs: $filesPath"
	if [[ ! -d "$CWD/java/resources/lib/$osName/" ]]; then
		\mkdir -pv "$CWD/java/resources/lib/$osName/" || exit 1
	fi
	# wild card
	if echo $filesFormat | \grep -q "*"; then
		filesPartA=`echo "$filesFormat" | \cut -f1 -d "*"`
		filesPartB=`echo "$filesFormat" | \cut -f2 -d "*"`
		\ls -lAsh "$filesPath/$filesPartA"*"$filesPartB" || exit 1
		\cp -afv  "$filesPath/$filesPartA"*"$filesPartB"  "$CWD/java/resources/lib/$osName/" || exit 1
	# single file
	else
		\ls -lAsh "$filesPath/$filesFormat" || exit 1
		# no rename
		if [[ -z $renameFile ]]; then
			\cp -afv  "$CWD/$filesPath/$filesFormat"  "$CWD/java/resources/lib/$osName/"             || exit 1
		# rename file
		else
			\cp -afv  "$CWD/$filesPath/$filesFormat"  "$CWD/java/resources/lib/$osName/$renameFile"  || exit 1
		fi
	fi
}



# win32 binaries are in win64 zip
CWD=`pwd`
if [[ ! -f "$CWD/$LIB_VERSIONS_PATH/win32-version.txt" ]]; then
	\cp -afv \
		"$CWD/$LIB_VERSIONS_PATH/win64-version.txt" \
		"$CWD/$LIB_VERSIONS_PATH/win32-version.txt" \
		|| exit 1
fi



#         <osName>   <libraries path>                             <libraries file format>   <optional rename format>
# linux64
CopyLibs  "linux64"  "libraries/linux64-<version>/release/build"  "libftd2xx.so.<version>"  "libftd2xx.so"

# linux32
CopyLibs  "linux32"  "libraries/linux32-<version>/release/build"  "libftd2xx.so.<version>"  "libftd2xx.so"

# win64
CopyLibs  "win64"    "libraries/win64-<version>/amd64"            "*.dll"

# win32
CopyLibs  "win32"    "libraries/win64-<version>/i386"             "*.dll"



# .h files
\cp -avf  "libraries/linux64-1.4.6/release/ftd2xx.h"    "natives/"  || exit 1
\cp -avf  "libraries/linux64-1.4.6/release/WinTypes.h"  "natives/"  || exit 1
\pushd natives/ >/dev/null || exit 1
	\dos2unix ftd2xx.h   || exit 1
	\dos2unix WinTypes.h || exit 1
\popd >/dev/null



\popd >/dev/null



#\pushd "natives/linux64/" >/dev/null || exit 1
#	echo -n "natives/linux64/ "
#	\ln -svf  "../../libraries/${linux64_lib_dir}release/ftd2xx.h"    ftd2xx.h    || exit 1
#	echo -n "natives/linux64/ "
#	\ln -svf  "../../libraries/${linux64_lib_dir}release/WinTypes.h"  WinTypes.h  || exit 1
#\popd >/dev/null
#
#\pushd "java/resources/" >/dev/null || exit 1
#	echo -n "java/resources/"
#	\cp -vf \
#		"../../libraries/${linux64_lib_dir}release/build/libftd2xx.so."* \
#		libftd2xx.so \
#		|| exit 1
#\popd >/dev/null



# mvn install:install-file -DgroupId=ftdi -DartifactId=ftdi-native -Dversion=1.4.6 -Dpackaging=so -Dfile=libftd2xx.so



echo
echo "Finished getting libraries!"
echo
