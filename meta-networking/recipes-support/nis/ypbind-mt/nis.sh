#!/bin/sh
#
# nis.sh: dhclient-script plugin for NIS settings,
#         place in /etc/dhcp/dhclient.d and 'chmod +x nis.sh' to enable
#
# Copyright (C) 2008 Red Hat, Inc.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
# Author(s): David Cantrell <dcantrell@redhat.com>

CONF=/etc/yp.conf
SAVECONF=${SAVEDIR}/${CONF##*/}.predhclient.${interface}

fix_context() {
    if [ -x /sbin/restorecon ]; then
        /sbin/restorecon ${1} >/dev/null 2>&1
    fi
}

save_config_file() {
    if [ ! -d ${SAVEDIR} ]; then
        mkdir -p ${SAVEDIR}
    fi

    if [ -e ${CONF} ]; then
        # cp+rm instead of mv: preserve SELinux context
        # rhbz#509240
        # Do not rely on restorecon.
        cp -c ${CONF} ${SAVECONF}
        rm ${CONF}
    else
        echo > ${SAVECONF}
        # Try restorecon
        fix_context ${SAVECONF}
    fi
}

nis_config() {
    if [ ! "${PEERNIS}" = "no" ]; then
        if [ -n "${new_nis_domain}" ]; then
            domainname "${new_nis_domain}"
            save_config_file
            let contents=0
            echo '# generated by /sbin/dhclient-script' > ${CONF}
            fix_context ${CONF}

            if [ -n "${new_nis_servers}" ]; then
                for i in ${new_nis_servers} ; do
                    echo "domain ${new_nis_domain} server ${i}" >> ${CONF}
                    let contents=contents+1
                done
            else
                echo "domain ${new_nis_domain} broadcast" >> ${CONF}
                let contents=contents+1
            fi

            if [ ${contents} -gt 0 ]; then
                service ypbind condrestart >/dev/null 2>&1
            fi
        elif [ -n "${new_nis_servers}" ]; then
            save_config_file
            echo '# generated by /sbin/dhclient-script' > ${CONF}
            fix_context ${CONF}
            let contents=0

            for i in ${new_nis_servers} ; do
                echo "ypserver ${i}" >> ${CONF}
                let contents=contents+1
            done

            if [ $contents -gt 0 ]; then
                service ypbind condrestart >/dev/null 2>&1
            fi
        fi
    fi
}

nis_restore() {
    if [ ! "${PEERNIS}" = "no" ]; then
        if [ -f ${SAVECONF} ]; then
            rm -f ${CONF}
            # cp+rm instead of mv: preserve SELinux context
            # rhbz#509240
            cp -c ${SAVECONF} ${CONF}
            rm ${SAVECONF}
            fix_context ${CONF} # Restorecon again to be sure.
            service ypbind condrestart >/dev/null 2>&1
        fi
    fi
}

# Local Variables:
# indent-tabs-mode: nil
# sh-basic-offset: 4
# show-trailing-whitespace: t
# End:
