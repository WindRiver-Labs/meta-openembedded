require lvm2.inc

PACKAGECONFIG ?= "odirect"

# Unset user/group to unbreak install.
EXTRA_OECONF = "--with-user= \
                --with-group= \
                --enable-pkgconfig \
                --with-usrlibdir=${libdir} \
"

TARGET_CC_ARCH += "${LDFLAGS}"

do_install_append() {
    # Remove things unrelated to libdevmapper
    rm -rf ${D}${sysconfdir}
    for i in `ls ${D}${sbindir}/*`; do
	if [ $i != ${D}${sbindir}/dmsetup ]; then
	    rm $i
	fi
    done
    # Remove docs
    rm -rf ${D}${datadir}
}

RRECOMMENDS_${PN} += "lvm2-udevrules"

