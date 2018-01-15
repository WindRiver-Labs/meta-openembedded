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

SRC_URI[GeoIP-dat.md5sum] = "58159ef6be54c1a5a033d07c9772b70e"
SRC_URI[GeoIP-dat.sha256sum] = "a99cbffa33515bdb0b8a2c4c3f5c853aa1d40408b0c0a49d2325f00022b3b54b"

SRC_URI[GeoIPv6-dat.md5sum] = "7a017634d34a1ef7a6de95088e9f1466"
SRC_URI[GeoIPv6-dat.sha256sum] = "78369d360f834cb160cf26fae26f22a1f4606d5b932555f5a6428049256928a9"

SRC_URI[GeoLiteCity-dat.md5sum] = "4e9b7e2ddf163ca8ee01b55eadf877cf"
SRC_URI[GeoLiteCity-dat.sha256sum] = "34cf284248e7da08efd33f7b084b635ff9dbde21555384bab14991552d6c4bed"

SRC_URI[GeoLiteCityv6-dat.md5sum] = "e2064dfd853125a60f342e94c8422d5e"
SRC_URI[GeoLiteCityv6-dat.sha256sum] = "1c223ab8756a253b040331e06b02f31fea928b7cb0d79688038efad78385f27f"

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
