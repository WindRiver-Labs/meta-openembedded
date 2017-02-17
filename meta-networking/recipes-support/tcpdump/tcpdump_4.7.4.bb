SUMMARY = "A sophisticated network protocol analyzer"
HOMEPAGE = "http://www.tcpdump.org/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1d4b0366557951c84a94fabe3529f867"
SECTION = "net"
DEPENDS = "libpcap"

SRC_URI = " \
    http://www.tcpdump.org/release/${BP}.tar.gz \
    file://unnecessary-to-check-libpcap.patch \
    file://tcpdump-configure-dlpi.patch \
    file://add-ptest.patch \
    file://tcpdump-CVE-2016-7974.patch;apply=no \
    file://tcpdump-CVE-2016-7992.patch;apply=no \
    file://tcpdump-CVE-2016-7973.patch;apply=no \
    file://tcpdump-CVE-2016-7933.patch;apply=no \
    file://tcpdump-CVE-2016-7925.patch;apply=no \
    file://tcpdump-CVE-2016-7974-2.patch;apply=no \
    file://tcpdump-CVE-2016-7975.patch;apply=no \
    file://tcpdump-CVE-2016-7922.patch \
    file://tcpdump-CVE-2016-7939.patch;apply=no \
    file://tcpdump-CVE-2016-7927.patch;apply=no \
    file://tcpdump-CVE-2016-7926.patch;apply=no \
    file://tcpdump-CVE-2016-7975-2.patch;apply=no \
    file://tcpdump-CVE-2016-7924.patch;apply=no \
    file://tcpdump-CVE-2016-7975-3.patch;apply=no \
    file://tcpdump-CVE-2016-7928.patch;apply=no \
    file://tcpdump-CVE-2016-7930.patch;apply=no \
    file://tcpdump-CVE-2016-7936.patch;apply=no \
    file://tcpdump-CVE-2016-7973-2.patch;apply=no \
    file://tcpdump-CVE-2016-7931.patch;apply=no \
    file://tcpdump-CVE-2016-7974-3.patch;apply=no \
    file://tcpdump-CVE-2016-7940.patch;apply=no \
    file://tcpdump-CVE-2016-7923.patch;apply=no \
    file://tcpdump-CVE-2016-7929.patch;apply=no \
    file://tcpdump-CVE-2016-7983-7984.patch;apply=no \
    file://tcpdump-CVE-2016-7993.patch;apply=no \
    file://tcpdump-CVE-2016-7938.patch \
    file://tcpdump-CVE-2016-7922-2.patch \
    file://0001-Fix-heuristic-not-to-be-byte-order-dependent.patch;apply=no \
    file://0001-Fetch-RTP-option-and-extension-data-using-EXTRACT_32.patch;apply=no \
    file://tcpdump-CVE-2016-7934-7935-7937.patch;apply=no \
    file://tcpdump-CVE-2017-5203.patch \
    file://tcpdump-CVE-2017-5202.patch \
    file://run-ptest \
"
SRC_URI[md5sum] = "58af728de36f499341918fc4b8e827c3"
SRC_URI[sha256sum] = "6be520269a89036f99c0b2126713a60965953eab921002b07608ccfc0c47d9af"
export LIBS=" -lpcap"

inherit autotools-brokensep ptest
CACHED_CONFIGUREVARS = "ac_cv_linux_vers=${ac_cv_linux_vers=2}"

PACKAGECONFIG ??= "openssl ipv6"
PACKAGECONFIG[openssl] = "--with-crypto=yes, --without-openssl --without-crypto, openssl"
PACKAGECONFIG[ipv6] = "--enable-ipv6, --disable-ipv6,"
PACKAGECONFIG[smi] = "--with-smi, --without-smi,libsmi"
PACKAGECONFIG[libcap-ng] = "--with-cap-ng=yes,--with-cap-ng=no,libcap-ng"

EXTRA_AUTORECONF += " -I m4"

do_git_apply () {
       cd ${S}
       if [ ! -f tests/heap-overflow-1.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7974.patch
       fi
       if [ ! -f tests/heap-overflow-2.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7992.patch
       fi
       if [ ! -f tests/heapoverflow-atalk_print.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7973.patch
       fi
       if [ ! -f tests/heapoverflow-ppp_hdlc_if_print.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7933.patch
       fi
       if [ ! -f tests/heapoverflow-sl_if_print.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7925.patch
       fi
       if [ ! -f tests/heapoverflow-ip_print_demux.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7974-2.patch
       fi
       if [ ! -f tests/heapoverflow-tcp_print.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7975.patch
       fi
       if [ ! -f tests/gre-heapoverflow-1.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7939.patch
       fi
       if [ ! -f tests/radiotap-heapoverflow.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7927.patch
       fi
       if [ ! -f tests/isoclns-heapoverflow.out ]; then
               git apply ${S}/../tcpdump-CVE-2016-7926.patch
       fi
       if [ ! -f tests/tcp-auth-heapoverflow.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7975-2.patch
       fi
       if [ ! -f tests/atm-oam-heapoverflow.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7924.patch
       fi
       if [ ! -f tests/tcp_header_heapoverflow.out ]; then
               git apply ${S}/../tcpdump-CVE-2016-7975-3.patch
       fi
       if [ ! -f tests/ipcomp-heapoverflow.out ]; then
               git apply ${S}/../tcpdump-CVE-2016-7928.patch
       fi
       if [ ! -f tests/llc-xid-heapoverflow.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7930.patch
       fi
       if [ ! -f tests/udp-length-heapoverflow.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7936.patch
       fi
       if [ ! -f tests/aarp-heapoverflow-1.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7973-2.patch
       fi
       if [ ! -f tests/mpls-label-heapoverflow.out ]; then
               git apply ${S}/../tcpdump-CVE-2016-7931.patch
       fi
       if [ ! -f tests/bad-ipv4-version-pgm-heapoverflow.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7974-3.patch
       fi
       if [ ! -f tests/stp-heapoverflow-1.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7940.patch
       fi
       if [ ! -f tests/arp-too-long-tha.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7923.patch
       fi
       if [ ! -f tests/juniper_header-heapoverflow.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7929.patch
       fi
       if [ ! -f tests/tftp-heapoverflow.pcap ]; then
               git apply ${S}/../tcpdump-CVE-2016-7983-7984.patch
               git apply ${S}/../tcpdump-CVE-2016-7993.patch
       fi
       if [ ! -f tests/rtp-seg-fault-1.pcap ]; then
               git apply ${S}/../0001-Fix-heuristic-not-to-be-byte-order-dependent.patch
               git apply ${S}/../0001-Fetch-RTP-option-and-extension-data-using-EXTRACT_32.patch
               git apply ${S}/../tcpdump-CVE-2016-7934-7935-7937.patch
       fi
}

do_patch_append() {
    bb.build.exec_func('do_git_apply', d)
}

do_configure_prepend() {
    mkdir -p ${S}/m4
    if [ -f aclocal.m4 ]; then
        mv aclocal.m4 ${S}/m4
    fi
    # AC_CHECK_LIB(dlpi.. was looking to host /lib
    sed -i 's:-L/lib::g' ./configure.in
}
do_configure_append() {
    sed -i 's:-L/usr/lib::' ./Makefile
    sed -i 's:-Wl,-rpath,${STAGING_LIBDIR}::' ./Makefile
    sed -i 's:-I/usr/include::' ./Makefile
}

do_install_append() {
    # tcpdump 4.0.0 installs a copy to /usr/sbin/tcpdump.4.0.0
    rm -f ${D}${sbindir}/tcpdump.${PV}
}

do_compile_ptest() {
    oe_runmake buildtest-TESTS
}
