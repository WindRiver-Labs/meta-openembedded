require lvm2.inc

SRC_URI += "file://0001-libdm-disable-installation-explicitly.patch"

CACHED_CONFIGUREVARS += "MODPROBE_CMD=${base_sbindir}/modprobe"

do_install_append() {
    # Install machine specific configuration file
    install -m 0644 ${WORKDIR}/lvm.conf ${D}${sysconfdir}/lvm/lvm.conf
    sed -i -e 's:@libdir@:${libdir}:g' ${D}${sysconfdir}/lvm/lvm.conf
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        oe_runmake 'DESTDIR=${D}' install install_systemd_units
        sed -i -e 's:/usr/bin/true:${base_bindir}/true:g' ${D}${systemd_system_unitdir}/blk-availability.service
    else
        oe_runmake 'DESTDIR=${D}' install install_initscripts
        mv ${D}${sysconfdir}/rc.d/init.d ${D}${sysconfdir}/init.d
        rm -rf ${D}${sysconfdir}/rc.d
    fi
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "lvm2-monitor.service dm-event.socket dm-event.service lvm2-lvmetad.socket \
                         lvm2-pvscan@.service blk-availability.service"
SYSTEMD_AUTO_ENABLE = "disable"

TARGET_CC_ARCH += "${LDFLAGS}"

PACKAGES =+ "${PN}-udevrules"
FILES_${PN}-udevrules = "${nonarch_base_libdir}/udev/rules.d"
FILES_${PN} += "${libdir}/device-mapper/*.so ${base_libdir}/udev"
FILES_${PN}-dbg += "${libdir}/device-mapper/.debug"

RDEPENDS_${PN} = "bash libdevmapper"

CONFFILES_${PN} += "${sysconfdir}/lvm/lvm.conf"
