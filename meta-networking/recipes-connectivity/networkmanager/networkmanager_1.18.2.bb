SUMMARY = "NetworkManager"
HOMEPAGE = "https://wiki.gnome.org/Projects/NetworkManager"
SECTION = "net/misc"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=cbbffd568227ada506640fe950a4823b \
                    file://libnm-util/COPYING;md5=1c4fa765d6eb3cd2fbd84344a1b816cd \
"

DEPENDS = " \
    intltool-native \
    libxslt-native \
    libnl \
    libgudev \
    util-linux \
    libndp \
    libnewt \
    jansson \
    curl \
"

inherit gnomebase gettext update-rc.d systemd bash-completion vala gobject-introspection gtk-doc upstream-version-is-even

SRC_URI = " \
    ${GNOME_MIRROR}/NetworkManager/${@gnome_verdir("${PV}")}/NetworkManager-${PV}.tar.xz \
    file://${BPN}.initd \
    file://0001-Fixed-configure.ac-Fix-pkgconfig-sysroot-locations.patch \
    file://0002-Do-not-create-settings-settings-property-documentati.patch \
"
SRC_URI_append_libc-musl = " \
    file://musl/0001-Fix-build-with-musl-systemd-specific.patch \
    file://musl/0002-Fix-build-with-musl.patch \
"
SRC_URI[md5sum] = "ca1e6175c6ba97ca1adf65a67861ccd9"
SRC_URI[sha256sum] = "4dd97ca974cd1f97990746527258f551f4257cbf011fecd01d10b7d74a6fa5c3"

S = "${WORKDIR}/NetworkManager-${PV}"

EXTRA_OECONF = " \
    --disable-ifcfg-rh \
    --disable-more-warnings \
    --with-iptables=${sbindir}/iptables \
    --with-tests \
    --with-nmtui=yes \
    --with-udev-dir=${nonarch_base_libdir}/udev \
"

# stolen from https://github.com/void-linux/void-packages/blob/master/srcpkgs/NetworkManager/template
# avoids:
# | ../NetworkManager-1.16.0/libnm-core/nm-json.c:106:50: error: 'RTLD_DEEPBIND' undeclared (first use in this function); did you mean 'RTLD_DEFAULT'?
CFLAGS_append_libc-musl = " \
    -DRTLD_DEEPBIND=0 \
"

do_compile_prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/libnm/.libs:${B}/libnm-glib/.libs:${B}/libnm-util/.libs"
}

PACKAGECONFIG ??= "nss ifupdown dhclient dnsmasq \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', bb.utils.contains('DISTRO_FEATURES', 'x11', 'consolekit', '', d), d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez5', '', d)} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wifi polkit', d)} \
"
PACKAGECONFIG[systemd] = " \
    --with-systemdsystemunitdir=${systemd_unitdir}/system --with-session-tracking=systemd, \
    --without-systemdsystemunitdir, \
"
PACKAGECONFIG[polkit] = "--enable-polkit --enable-polkit-agent,--disable-polkit --disable-polkit-agent,polkit"
PACKAGECONFIG[bluez5] = "--enable-bluez5-dun,--disable-bluez5-dun,bluez5"
# consolekit is not picked by shlibs, so add it to RDEPENDS too
PACKAGECONFIG[consolekit] = "--with-session-tracking=consolekit,,consolekit,consolekit"
PACKAGECONFIG[modemmanager] = "--with-modem-manager-1=yes,--with-modem-manager-1=no,modemmanager"
PACKAGECONFIG[ppp] = "--enable-ppp,--disable-ppp,ppp,ppp"
# Use full featured dhcp client instead of internal one
PACKAGECONFIG[dhclient] = "--with-dhclient=${base_sbindir}/dhclient,,,dhcp-client"
PACKAGECONFIG[dnsmasq] = "--with-dnsmasq=${bindir}/dnsmasq"
PACKAGECONFIG[nss] = "--with-crypto=nss,,nss"
PACKAGECONFIG[resolvconf] = "--with-resolvconf=${base_sbindir}/resolvconf,,,resolvconf"
PACKAGECONFIG[gnutls] = "--with-crypto=gnutls,,gnutls"
PACKAGECONFIG[wifi] = "--enable-wifi=yes,--enable-wifi=no,,wpa-supplicant"
PACKAGECONFIG[ifupdown] = "--enable-ifupdown,--disable-ifupdown"
PACKAGECONFIG[qt4-x11-free] = "--enable-qt,--disable-qt,qt4-x11-free"

PACKAGES =+ " \
  ${PN}-nmtui ${PN}-nmtui-doc \
  ${PN}-adsl \
"

FILES_${PN}-adsl = "${libdir}/NetworkManager/${PV}/libnm-device-plugin-adsl.so"

FILES_${PN} += " \
    ${libexecdir} \
    ${libdir}/NetworkManager/${PV}/*.so \
    ${nonarch_libdir}/NetworkManager/VPN \
    ${nonarch_libdir}/NetworkManager/conf.d \
    ${datadir}/polkit-1 \
    ${datadir}/dbus-1 \
    ${nonarch_base_libdir}/udev/* \
    ${systemd_unitdir}/system \
    ${libdir}/pppd \
"

RRECOMMENDS_${PN} += "iptables \
    ${@bb.utils.filter('PACKAGECONFIG', 'dnsmasq', d)} \
"
RCONFLICTS_${PN} = "connman"

FILES_${PN}-dev += " \
    ${datadir}/NetworkManager/gdb-cmd \
    ${libdir}/pppd/*/*.la \
    ${libdir}/NetworkManager/*.la \
    ${libdir}/NetworkManager/${PV}/*.la \
"

FILES_${PN}-nmtui = " \
    ${bindir}/nmtui \
    ${bindir}/nmtui-edit \
    ${bindir}/nmtui-connect \
    ${bindir}/nmtui-hostname \
"

FILES_${PN}-nmtui-doc = " \
    ${mandir}/man1/nmtui* \
"

INITSCRIPT_NAME = "network-manager"
SYSTEMD_SERVICE_${PN} = "${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'NetworkManager.service NetworkManager-dispatcher.service', '', d)}"

do_install_append() {
    install -Dm 0755 ${WORKDIR}/${BPN}.initd ${D}${sysconfdir}/init.d/network-manager

    rm -rf ${D}/run ${D}${localstatedir}/run
}
