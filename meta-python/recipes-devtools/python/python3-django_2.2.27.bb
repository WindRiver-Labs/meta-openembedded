require python-django.inc
inherit setuptools3

SRC_URI[sha256sum] = "1ee37046b0bf2b61e83b3a01d067323516ec3b6f2b17cd49b1326dd4ba9dc913"

RDEPENDS_${PN} += "\
    ${PYTHON_PN}-sqlparse \
"
