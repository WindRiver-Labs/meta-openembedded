DESCRIPTION = "OpenJPEG library is an open-source JPEG 2000 codec"
HOMEPAGE = "http://www.openjpeg.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c648878b4840d7babaade1303e7f108c"

SRC_URI = "git://github.com/uclouvain/openjpeg.git \
          "
S = "${WORKDIR}/git"
SRCREV = "16b701659d7d9f72aade1d695818a645e2f603a6"
inherit cmake

DEPENDS = "libpng tiff lcms zlib"

# standard path for *.cmake
EXTRA_OECMAKE += "-DOPENJPEG_INSTALL_PACKAGE_DIR=${baselib}/cmake \
                  -DOPENJPEG_INSTALL_LIB_DIR:PATH=${libdir}"

FILES_${PN}-dev += "${libdir}/cmake/*.cmake"
