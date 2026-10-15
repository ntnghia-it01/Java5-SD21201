#!/bin/sh
set -eu

# Chỉ nhận identifier đơn giản để tên DB an toàn trong SQL và JDBC URL.
: "${DB_NAME:?Set DB_NAME in .env}"
case "$DB_NAME" in
  [0-9]*|*[!A-Za-z0-9_]*|'')
    echo "DB_NAME must start with a letter or underscore and contain only A-Z, a-z, 0-9, _." >&2
    exit 1
    ;;
esac
if [ "${#DB_NAME}" -gt 128 ]; then
  echo "DB_NAME must be at most 128 characters." >&2
  exit 1
fi

exec /opt/mssql-tools18/bin/sqlcmd \
  -S sqlserver -U sa -C -b \
  -v DB_NAME="$DB_NAME" \
  -i /init/init-db.sql
