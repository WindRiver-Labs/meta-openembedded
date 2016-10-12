SUMMARY = "XFS Filesystem Utilities"
HOMEPAGE = "http://oss.sgi.com/projects/xfs"
SECTION = "base"
LICENSE = "GPLv2 & LGPLv2.1"
LICENSE_libhandle = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://doc/COPYING;md5=dbdb5f4329b7e7145de650e9ecd4ac2a"
DEPENDS = "util-linux"

SRC_URI = "ftp://oss.sgi.com/projects/xfs/cmd_tars/${BP}.tar.gz \
    file://xfsprogs-generate-crctable-which-is-moved-into-runti.patch \
    file://remove-install-as-user.patch \
"

SRC_URI[md5sum] = "ae82b0ab63e89cfda52fb9859855bafa"
SRC_URI[sha256sum] = "88580bb3e6847c3edef436703a4fae403fc19b20739db4c31166ee4b256178d7"

inherit autotools-brokensep

PACKAGES =+ "${PN}-fsck ${PN}-mkfs libhandle"

RDEPENDS_${PN} = "${PN}-fsck ${PN}-mkfs"

FILES_${PN}-fsck = "${base_sbindir}/fsck.xfs"
FILES_${PN}-mkfs = "${base_sbindir}/mkfs.xfs"
FILES_libhandle = "${base_libdir}/libhandle${SOLIBS}"

EXTRA_OECONF = "--enable-gettext=no"
do_configure () {
    # Prevent Makefile from calling configure without arguments,
    # when do_configure gets called for a second time.
    rm -f include/builddefs include/platform_defs.h
    # Recreate configure script.
    rm -f configure
    oe_runmake configure
    # Configure.
    export DEBUG="-DNDEBUG"
    gnu-configize --force
    oe_runconf
}

LIBTOOL = "${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"
TARGET_CC_ARCH += "${LDFLAGS}"
PARALLEL_MAKE = ""

do_install () {
    export DIST_ROOT=${D}
    oe_runmake install
    # needed for xfsdump
    oe_runmake install-dev
}
