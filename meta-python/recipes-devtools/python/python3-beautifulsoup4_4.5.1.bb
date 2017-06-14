SUMMARY = "Screen-scraping library"
HOMEPAGE = " https://www.crummy.com/software/BeautifulSoup/bs4"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=7a45a67063d2bfb7832d52d5ce94ed39"

SRC_URI[md5sum] = "994abd90e691beaf7d42c00ffb2f3a67"
SRC_URI[sha256sum] = "3c9474036afda9136aac6463def733f81017bf9ef3510d25634f335b0c87f5e1"

inherit pypi setuptools3

BBCLASSEXTEND = "nativesdk"

RDEPENDS_${PN} = "\
    python3-core \
"
