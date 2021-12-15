require python-django.inc
inherit setuptools3

SRC_URI[sha256sum] = "b1e65eaf371347d4b13eb7e061b09786c973061de95390c327c85c1e2aa2349c"

RDEPENDS_${PN} += "\
    ${PYTHON_PN}-sqlparse \
"
