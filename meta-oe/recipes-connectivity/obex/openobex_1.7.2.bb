DESCRIPTION = "The Openobex project is an open source implementation of the \
Object Exchange (OBEX) protocol."
HOMEPAGE = "http://openobex.triq.net"
SECTION = "libs"
DEPENDS = "virtual/libusb0"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES','bluez5','bluez5','bluez4',d)}"

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://COPYING.LIB;md5=a6f89e2100d9b6cdffcea4f398e37343 \
"

SRC_URI = "http://downloads.sourceforge.net/openobex/openobex-${PV}-Source.tar.gz \
           file://0001-obex-check-device-avoid-segment-fault.patch"

SRC_URI[md5sum] = "f6e0b6cb7dcfd731460a7e9a91429a3a"
SRC_URI[sha256sum] = "158860aaea52f0fce0c8e4b64550daaae06df2689e05834697b7e8c7d73dd4fc"

inherit cmake

S = "${WORKDIR}/openobex-${PV}-Source"

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release"

do_install_append () {
    if [ "`ls -A ${D}${bindir}`" = "" ]; then
	rm -rf ${D}${bindir}
    fi
}

FILES_${PN}-dev += "${libdir}/cmake"


