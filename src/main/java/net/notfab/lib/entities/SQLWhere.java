package net.notfab.lib.entities;

import net.notfab.lib.Dialect;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

public class SQLWhere {

    private List<SQLFilter> filterList;

    public SQLWhere(SQLFilter... filters) {
        this.filterList = Arrays.asList(filters);
    }

    public String toString(Dialect dialect) {
        StringBuilder where = new StringBuilder();
        filterList.forEach(q -> {
            where.append(" AND ").append(q.toString(dialect));
        });
        return where.toString().replaceFirst(" AND ", " WHERE ");
    }

    @Override
    public String toString() {
        return this.toString(Dialect.SQLite);
    }

    public Query prepare(Query query) {
        final int[] i = {1};
        filterList.forEach(f -> {
            if (f instanceof SQLMultiFilter) {
                SQLMultiFilter multi = (SQLMultiFilter) f;
                for (String value : multi.getValues()) {
                    query.setParameter(i[0], value);
                    i[0]++;
                }
            } else if (!(f instanceof SQLRaw)) {
                query.setParameter(i[0], f.getValue());
                i[0]++;
            }
        });
        return query;
    }

}