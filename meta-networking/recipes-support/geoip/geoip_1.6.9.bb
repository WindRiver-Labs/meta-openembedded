SUMMARY = "C library for country/city/organization to IP address or hostname mapping"
DESCRIPTION = "GeoIP is a C library that enables the user to find the country that any IP \
address or hostname originates from. It uses a file based database that is \
accurate as of March 2003. This database simply contains IP blocks as keys, and \
countries as values. This database should be more complete and accurate than \
using reverse DNS lookups."

HOMEPAGE = "http://dev.maxmind.com/geoip/"
SECTION = "libdevel"

SRC_URI = "git://github.com/maxmind/geoip-api-c.git \
           http://geolite.maxmind.com/download/geoip/database/GeoLiteCountry/GeoIP.dat.gz;apply=no;name=GeoIP-dat \
           http://geolite.maxmind.com/download/geoip/database/GeoIPv6.dat.gz;apply=no;name=GeoIPv6-dat \
           http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz;apply=no;name=GeoLiteCity-dat \
           http://geolite.maxmind.com/download/geoip/database/GeoLiteCityv6-beta/GeoLiteCityv6.dat.gz;apply=no;name=GeoLiteCityv6-dat \
           file://run-ptest \
"
SRCREV = "4f487bf95532e0bba7783d591faff178ab0aa462"

SRC_URI[GeoIP-dat.md5sum] = "9a9b21a7553753cbfd2b9a36c983d1ed"
SRC_URI[GeoIP-dat.sha256sum] = "eb1c7c8275a98df4e94966ce0af6260b0b9c20995a09a43941cd49ed880d6424"

SRC_URI[GeoIPv6-dat.md5sum] = "f0cb67841692cb3451565c3cb54ac1f8"
SRC_URI[GeoIPv6-dat.sha256sum] = "103bcb3b7b7b33db879d57631f31a195b14c793fd3e7a26d877a7e77918f8c8b"

SRC_URI[GeoLiteCity-dat.md5sum] = "8dab966a8ea4ecc00a4264a01f1cb61f"
SRC_URI[GeoLiteCity-dat.sha256sum] = "4a47770de542db6799c039f6637c1d96ebb41f3966b23f1f61d548b7f8729693"

SRC_URI[GeoLiteCityv6-dat.md5sum] = "3b1a76b46dbcc667d39c5ac45cf93029"
SRC_URI[GeoLiteCityv6-dat.sha256sum] = "3ee04557373e4729983b81ef70fed0333ee66bcaba635983794fb43ce44b754e"

LICENSE = "LGPL-2.1"

LIC_FILES_CHKSUM = "file://COPYING;md5=243b725d71bb5df4a1e5920b344b86ad \
                    file://LICENSE;md5=0388276749a542b0d611601fa7c1dcc8 "

S = "${WORKDIR}/git"

inherit autotools

EXTRA_OECONF = "--disable-static               \
                --disable-dependency-tracking  "

do_install() {
    make DESTDIR=${D} install
    install -d ${D}/${datadir}/GeoIP
    install ${WORKDIR}/GeoIP.dat ${D}/${datadir}/GeoIP/
    install ${WORKDIR}/GeoIPv6.dat ${D}/${datadir}/GeoIP/
    install ${WORKDIR}/GeoLiteCity.dat ${D}/${datadir}/GeoIP/
    install ${WORKDIR}/GeoLiteCityv6.dat ${D}/${datadir}/GeoIP/
    ln -s GeoLiteCity.dat ${D}${datadir}/GeoIP/GeoIPCity.dat
}

PACKAGES =+ "${PN}-database"
FILES_${PN}-database = ""
FILES_${PN}-database += "${datadir}/GeoIP/*"

# We cannot do much looking up without databases.
#
RDEPENDS_${PN} += "${PN}-database"

inherit ptest

do_configure_ptest() {
    sed -i -e "s/noinst_PROGRAMS = /test_PROGRAMS = /g" \
        -e 's:SRCDIR=\\"$(top_srcdir)\\":SRCDIR=\\"$(testdir)\\":' \
        ${S}/test/Makefile.am

    if ! grep "^testdir = " ${S}/test/Makefile.am ; then
        sed -e '/EXTRA_PROGRAMS = /itestdir = ${PTEST_PATH}/tests' \
            -i ${S}/test/Makefile.am
    fi

    sed -i -e "s:/usr/local/share:/usr/share:g" \
        ${S}/test/benchmark.c

    sed -i -e 's:"../data/:"/usr/share/GeoIP/:g' \
        ${S}/test/test-geoip-city.c \
        ${S}/test/test-geoip-isp.c \
        ${S}/test/test-geoip-asnum.c \
        ${S}/test/test-geoip-netspeed.c \
        ${S}/test/test-geoip-org.c \
        ${S}/test/test-geoip-region.c
}


do_install_ptest() {
    oe_runmake -C test DESTDIR=${D}  install-testPROGRAMS
    install ${S}/test/*.txt ${D}${PTEST_PATH}/tests
}
