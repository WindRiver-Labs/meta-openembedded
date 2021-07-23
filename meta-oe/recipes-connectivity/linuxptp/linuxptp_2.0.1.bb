DESCRIPTION = "Precision Time Protocol (PTP) according to IEEE standard 1588 for Linux"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "https://sourceforge.net/projects/linuxptp/files/v2.0/linuxptp-${PV}.tgz \
           file://build-Allow-CC-and-prefix-to-be-overriden.patch \
           file://no-incdefs-using-host-headers.patch \
           "

SRC_URI[md5sum] = "896b590b868105bb3838f9c126cf1ae6"
SRC_URI[sha256sum] = "6f4669db1733747427217a9e74c8b5ca25c4245947463e9cdb860ec8f5ec797a"

EXTRA_OEMAKE = "ARCH=${TARGET_ARCH} \
    EXTRA_CFLAGS='-D_GNU_SOURCE -DHAVE_CLOCK_ADJTIME -DHAVE_POSIX_SPAWN -DHAVE_ONESTEP_SYNC ${CFLAGS}'"

do_install () {
    install -d ${D}/${bindir}
    install -p ${S}/ptp4l  ${D}/${bindir}
    install -p ${S}/pmc  ${D}/${bindir}
    install -p ${S}/phc2sys  ${D}/${bindir}
    install -p ${S}/hwstamp_ctl  ${D}/${bindir}
}
