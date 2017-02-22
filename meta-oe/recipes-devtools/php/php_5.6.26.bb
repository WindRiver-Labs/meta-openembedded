require php.inc

LIC_FILES_CHKSUM = "file://LICENSE;md5=b602636d46a61c0ac0432bbf5c078fe4"

SRC_URI += "file://change-AC_TRY_RUN-to-AC_TRY_LINK.patch \
            file://php-CVE-2016-10160.patch \
            file://php-CVE-2016-9137.patch \
            file://php-CVE-2016-9935.patch \
            file://php-CVE-2016-10158.patch \
            file://php-CVE-2016-10161.patch \
            file://php-CVE-2016-10159.patch \
            file://php-CVE-2016-7479.patch \
            file://php-CVE-2016-9934.patch \
            file://php-CVE-2016-9933.patch \
"

SRC_URI[md5sum] = "cb424b705cfb715fc04f499f8a8cf52e"
SRC_URI[sha256sum] = "d47aab8083a4284b905777e1b45dd7735adc53be827b29f896684750ac8b6236"
