#
# Copyright (C) 2020 Wind River Systems, Inc.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
SUMMARY = "intel® Speed Select Technology (Intel® SST)"

DESCRIPTION = "Intel SST is active and nuanced control over CPU performance \
tools about CPU core count, workload, Tjmax, and TDP tools to examine and tune \
power."

LICENSE = "GPLv2"
COMPATIBLE_HOST = '(x86_64.*|i.86.*)-linux'
PNWHITELIST_openembedded-layer += 'intel-speed-select'

PACKAGE_ARCH = "${MACHINE_ARCH}"
DEPENDS = "virtual/kernel"

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"

# This looks in S, so we better make sure there's
# something in the directory.

do_populate_lic[depends] += "virtual/kernel:do_patch"

SST_SRC ?= "Makefile \
	    tools/build \
	    tools/scripts \
	    tools/power/x86/intel-speed-select \
	    include/uapi/linux/isst_if.h \
"

EXTRA_OEMAKE = '\
    -C ${S}/tools/power/x86/intel-speed-select \
     O=${B} \
     CROSS_COMPILE=${TARGET_PREFIX} \
     ARCH=${ARCH} \
     CC="${CC}" \
     AR="${AR}" \
     LD="${LD}" \
'

do_configure[prefuncs] += "mkdir_nonexistent_before"
mkdir_nonexistent_before() {
	mkdir -p ${S}/tools/power/x86/intel-speed-select/include/linux/
	mkdir -p ${S}/include/linux/
	mkdir -p ${S}/include/uapi/linux/
}

do_configure[prefuncs] += "copy_sst_source_from_kernel"
python copy_sst_source_from_kernel() {
    sources = (d.getVar("SST_SRC") or "").split()
    src_dir = d.getVar("STAGING_KERNEL_DIR")
    dest_dir = d.getVar("S")
    bb.utils.mkdirhier(dest_dir)
    for s in sources:
        src = oe.path.join(src_dir, s)
        dest = oe.path.join(dest_dir, s)
        if not os.path.exists(src):
            bb.fatal("Path does not exist: %s. Maybe SST_SRC does not match the kernel version." % src)
        if os.path.isdir(src):
            oe.path.copyhardlinktree(src, dest)
        else:
           bb.utils.copyfile(src, dest)
}


do_compile() {
	oe_runmake STAGING_KERNEL_DIR=${STAGING_KERNEL_DIR}
}

do_install() {
	oe_runmake DESTDIR="${D}" install
}
