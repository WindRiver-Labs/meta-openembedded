SUMMARY = "Web-based MySQL administration interface"
HOMEPAGE = "http://www.phpmyadmin.net"
# Main code is GPLv2, libraries/tcpdf is under LGPLv3, js/jquery is under MIT
LICENSE = "GPLv2 & LGPLv3 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://libraries/tcpdf/LICENSE.TXT;md5=5c87b66a5358ebcc495b03e0afcd342c"

SRC_URI = "https://files.phpmyadmin.net/phpMyAdmin/${PV}/phpMyAdmin-${PV}-all-languages.tar.xz \
           file://CVE-2016-6625.patch \
           file://CVE-2016-6622.patch \
           file://CVE-2016-6620.patch \
           file://CVE-2016-9849.patch \
           file://CVE-2016-9847.patch \
           file://CVE-2016-6619.patch \
           file://CVE-2016-6618.patch \
           file://CVE-2016-6615.patch \
           file://CVE-2016-6615-2.patch \
           file://CVE-2016-6615-3.patch \
           file://CVE-2016-6614.patch \
           file://CVE-2016-6617.patch \
           file://CVE-2016-6616.patch \
           file://CVE-2016-6616-2.patch \
           file://CVE-2016-6610.patch \
           file://CVE-2016-6613.patch \
           file://CVE-2016-6612.patch \
           file://CVE-2016-9858.patch \
           file://CVE-2016-9858-2.patch \
           file://CVE-2016-9858-3.patch \
           file://CVE-2016-9852.patch \
           file://CVE-2016-9852-2.patch \
           file://CVE-2016-9850.patch \
           file://CVE-2016-9850-2.patch \
           file://CVE-2016-9851.patch \
           file://CVE-2016-9856.patch \
           file://CVE-2016-9856-2.patch \
           file://CVE-2016-6632.patch \
           file://CVE-2016-6611.patch \
           file://CVE-2016-6608.patch \
           file://CVE-2016-6608-2.patch \
           file://CVE-2016-6608-3.patch \
           file://CVE-2016-6608-4.patch \
           file://CVE-2016-6606.patch \
           file://CVE-2016-9866.patch \
           file://CVE-2016-9865.patch \
           file://CVE-2016-9864.patch \
           file://CVE-2016-9864-2.patch \
           file://CVE-2016-9863.patch \
           file://CVE-2016-9862.patch \
           file://CVE-2016-6633.patch \
           file://CVE-2016-6630.patch \
           file://CVE-2016-6609.patch \
           file://CVE-2016-6624.patch \
           file://CVE-2016-6627.patch \
           file://CVE-2016-6623.patch \
           file://CVE-2016-6628.patch \
           file://0001-Add-tests-for-PMA_isAllowedDomain.patch \
           file://CVE-2016-6626.patch \
           file://CVE-2016-9861.patch \
           file://CVE-2016-6631.patch \
           file://apache.conf \
"

SRC_URI[md5sum] = "53c7a6a577d10de04a5dd21a05018542"
SRC_URI[sha256sum] = "943bad38a95f21bb015bdb78c9c067e0ea7510c1b35d4b8e757cb89c413e3bac"

S = "${WORKDIR}/phpMyAdmin-${PV}-all-languages"

inherit allarch

do_install() {
    install -d ${D}${datadir}/${BPN}
    cp -R --no-dereference --preserve=mode,links -v * ${D}${datadir}/${BPN}
    chown -R root:root ${D}${datadir}/${BPN}
    # Don't install patches to target
    rm -rf ${D}${datadir}/${BPN}/patches

    install -d ${D}${sysconfdir}/apache2/conf.d
    install -m 0644 ${WORKDIR}/apache.conf ${D}${sysconfdir}/apache2/conf.d/phpmyadmin.conf

    # Remove a few scripts that explicitly require bash (!)
    rm -f ${D}${datadir}/phpmyadmin/libraries/transformations/*.sh
}

FILES_${PN} = "${datadir}/${BPN} \
               ${sysconfdir}/apache2/conf.d"

RDEPENDS_${PN} += "bash"
