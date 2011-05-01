/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sampledb.shared;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/*
 *
 db2 describe table employee
 EMPNO                           SYSIBM    CHARACTER                    6     0 No    
 FIRSTNME                        SYSIBM    VARCHAR                     12     0 No    
 MIDINIT                         SYSIBM    CHARACTER                    1     0 Yes   
 LASTNAME                        SYSIBM    VARCHAR                     15     0 No    
 WORKDEPT                        SYSIBM    CHARACTER                    3     0 Yes   
 PHONENO                         SYSIBM    CHARACTER                    4     0 Yes   
 HIREDATE                        SYSIBM    TIMESTAMP                    7     0 Yes   
 JOB                             SYSIBM    CHARACTER                    8     0 Yes   
 EDLEVEL                         SYSIBM    SMALLINT                     2     0 No    
 SEX                             SYSIBM    CHARACTER                    1     0 Yes   
 BIRTHDATE                       SYSIBM    TIMESTAMP                    7     0 Yes   
 SALARY                          SYSIBM    DECIMAL                      9     2 Yes   
 BONUS                           SYSIBM    DECIMAL                      9     2 Yes   
 COMM                            SYSIBM    DECIMAL                      9     2 Yes 
 */

public class EmployeeRecord implements Serializable,IRecord {

    private String empno;
    private String firstname;
    private String midinit;
    private String lastname;
    private String workdept;
    private String phoneno;
    private Timestamp hiredate;
    private String job;
    private int edlevel;
    private String sex;
    private Timestamp birthdate;
    private BigDecimal salary;
    private BigDecimal bonus;
    private BigDecimal comm;

    public String getEmpno() {
        return empno;
    }

    public void setEmpno(String empno) {
        this.empno = empno;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMidinit() {
        return midinit;
    }

    public void setMidinit(String midinit) {
        this.midinit = midinit;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getWorkdept() {
        return workdept;
    }

    public void setWorkdept(String workdept) {
        this.workdept = workdept;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public Timestamp getHiredate() {
        return hiredate;
    }

    public void setHiredate(Timestamp hiredate) {
        this.hiredate = hiredate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getEdlevel() {
        return edlevel;
    }

    public void setEdlevel(int edlevel) {
        this.edlevel = edlevel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Timestamp getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getComm() {
        return comm;
    }

    public void setComm(BigDecimal comm) {
        this.comm = comm;
    }

}
