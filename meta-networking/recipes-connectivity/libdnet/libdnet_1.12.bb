SUMMARY = "dumb networking library"
HOMEPAGE = "http://code.google.com/p/libdnet/"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0036c1b155f4e999f3e0a373490b5db9"

SRC_URI = "https://github.com/dugsong/${BPN}/archive/${BP}.tar.gz"

SRC_URI[md5sum] = "d2f1b72eac2a1070959667e9e61dcf20"
SRC_URI[sha256sum] = "b6360659c93fa2e3cde9e0a1fc9c07bc4111f3448c5de856e095eb98315dd424"

S = "${WORKDIR}/${BPN}-${BP}"

inherit autotools

acpaths = "-I ./config/"

