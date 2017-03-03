SUMMARY = "C++ library for client-side URL transfers"
HOMEPAGE = "http://www.curlpp.org/"
SECTION = "libdevel"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DEPENDS = "curl boost"
DEPENDS_class-native = "curl-native"

SRC_URI = "https://github.com/jpbarrette/${BPN}/archive/v${PV}.tar.gz;downloadfilename=${BP}.tar.gz \
    file://example21.cpp-remove-deprecated-code.patch \
"

SRC_URI[md5sum] = "ee05f248bf3bbf7f381d65cf2d0ee50f"
SRC_URI[sha256sum] = "b72093f221a9e2d0f7ce0bd0f846587835e01607a7bb0f106ff4317a8c30a81c"

inherit autotools-brokensep pkgconfig binconfig

EXTRA_OECONF = "--with-boost=${STAGING_DIR_HOST}${prefix}"
# Upstream is currently working on porting the code to use std::unique_ptr instead of the
# deprecated auto_ptr.  For now, ignore the issue.
CXXFLAGS += "-Wno-error=deprecated-declarations"

do_install_append () {
    sed -i 's,${STAGING_DIR_TARGET},,g' ${D}${libdir}/pkgconfig/curlpp.pc
}

PACKAGES =+ "libcurlpp libcurlpp-dev libcurlpp-staticdev"
RPROVIDES_lib${BPN} = "${PN}"

FILES_lib${BPN} = "${libdir}/lib*.so.*"

FILES_lib${BPN}-dev = "${includedir} \
    ${libdir}/lib*.la \
    ${libdir}/pkgconfig \
    ${bindir}/*-config \
"

FILES_lib${BPN}-staticdev = "${libdir}/lib*.a"

BBCLASSEXTEND = "native nativesdk"
