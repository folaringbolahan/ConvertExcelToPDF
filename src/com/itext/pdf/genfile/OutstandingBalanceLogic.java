/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.genfile;

import com.itext.pdf.dao.DaoConnection;
import com.itext.pdf.model.MemberProfile;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author emmanuel.idoko
 */
public class OutstandingBalanceLogic {

    public static void main(String[] args) throws Exception {
        DaoConnection con_model = new DaoConnection();

        java.sql.Connection con_sql = con_model.SqlServerConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "select id,total_payable_amount from LOAN_APPLICATION where cooperative_id = 3 and id not in (38,39,55,57,59,88)";
        ps = con_sql.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        rs = ps.executeQuery();

        int size = 0;
        if (rs != null) {
            rs.last();    // moves cursor to the last row
            size = rs.getRow(); // get row id 
        }
        System.out.println("size of applications::::" + size);
        rs.beforeFirst();

        while (rs.next()) {
            //run query to get all schedules tied to the application
            int app_id = rs.getInt("id");
            System.out.println("application id..." + app_id);
            BigDecimal total_payable_amount = rs.getBigDecimal("total_payable_amount");
            BigDecimal outstandingBalance = total_payable_amount;
            System.out.println("total_payable_amount::::" + outstandingBalance);

            String schedule_query = "select * from LOAN_SCHEDULE where application_id = ? ";
            PreparedStatement ps_schedule = con_sql.prepareStatement(schedule_query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ps_schedule.setInt(1, app_id);

            ResultSet rs_schedule = ps_schedule.executeQuery();

            //get total number of loan schedules returned
            //end here
            int count = 0;
            int rowcount = 0;
            if (rs_schedule != null) {
                rs_schedule.last();
                rowcount = rs_schedule.getRow();
                rs_schedule.beforeFirst();
                //rs_schedule.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            System.out.println("loan application with id - " + app_id + " has " + rowcount + " number of schedules. ");

            while (rs_schedule.next()) {
                count++;
                BigDecimal amount_payable = rs_schedule.getBigDecimal("amount_payable");
                System.out.println("loan schedule id - " + rs_schedule.getInt("id") + " amount payable " + amount_payable+", index::"+count);

                String update = "update LOAN_SCHEDULE set outstanding_balance=? where id=?";
                PreparedStatement ps_u = con_sql.prepareStatement(update);
                ps_u.setBigDecimal(1, outstandingBalance.subtract(amount_payable.multiply(BigDecimal.valueOf(count))));
                ps_u.setInt(2, rs_schedule.getInt("id"));
                ps_u.executeUpdate();
                ps_u.close();

            }
            count = 0;
            System.out.println("Done updating loan schedule outstanding balance for application : " + app_id + ", count:::" + count);

        }
    }
}
