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

SRC_URI[GeoIP-dat.md5sum] = "d538e57ad9268fdc7955c6cf9a37c4a9"
SRC_URI[GeoIP-dat.sha256sum] = "b9c05eb8bfcf90a6ddfdc6815caf40a8db2710f0ce3dd48fbd6c24d485ae0449"

SRC_URI[GeoIPv6-dat.md5sum] = "c019ddd52c87d4f05bc13cf858a22f8e"
SRC_URI[GeoIPv6-dat.sha256sum] = "1072c972cb079871a774f333bfd12117a21c10552dc84ef147d400727b3ef79c"

SRC_URI[GeoLiteCity-dat.md5sum] = "d700c137232f8e077ac8db8577f699d9"
SRC_URI[GeoLiteCity-dat.sha256sum] = "90db2e52195e3d1bcdb2c2789209006d09de5c742812dbd9a1b36c12675ec4cd"

SRC_URI[GeoLiteCityv6-dat.md5sum] = "bc6c9ba16fe9a063588db7b3e3603137"
SRC_URI[GeoLiteCityv6-dat.sha256sum] = "fc93d461a80ba7452ffb9f166c1d82e6df27886a11faeafb32f6dcb741c3503c"

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
