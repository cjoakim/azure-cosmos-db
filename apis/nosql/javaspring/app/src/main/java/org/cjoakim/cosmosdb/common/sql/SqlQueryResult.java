package org.cjoakim.cosmosdb.common.sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.cjoakim.cosmosdb.common.CommonConstants;

import java.util.ArrayList;
import java.util.Map;

/**
 * Instances of this represent the results of a CosmosDB SQL query, including:
 * - the SQL
 * - the result items
 * - the associated Request Unit (RU) costs of the query
 * - the elapsed time in milliseconds
 *
 * Chris Joakim, Microsoft
 */

@Data
public class SqlQueryResult implements CommonConstants {

    // Instance variables:
    public String sql;
    public String method;
    public ArrayList<Object> items;
    public double totalRequestUnits;
    public long startTime;
    public long finishTime;

    public SqlQueryResult(String sql) {

        super();
        this.sql = sql;
        this.items = new ArrayList<Object>();
        this.totalRequestUnits = 0.0;
        startTimer();
    }

    public Object getItem(int index) {

        return items.get(index);
    }

    public int getItemCount() {

        return items.size();
    }
    public void addItem(Object item) {

        items.add(item);
    }
    public void incrementTotalRequestUnits(double d) {

        this.totalRequestUnits = this.totalRequestUnits + d;
    }
    public void startTimer() {

        this.startTime = System.currentTimeMillis();
    }
    public long stopTimer() {

        this.finishTime = System.currentTimeMillis();
        return getElapsedMs();
    }
    public long getElapsedMs() {

        return finishTime - startTime;
    }

    @JsonIgnore
    public long getCountResult() {

        try {
            Map firstItem = (Map) items.get(0);
            return (long) firstItem.get("count");
        }
        catch (Exception e) {
            return -1;
        }
    }
    public String toJson(boolean pretty) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (pretty) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            }
            else {
                return mapper.writeValueAsString(this);
            }
        }
        catch (JsonProcessingException e) {
            return null;
        }
    }
}
