package com.example.HRMSAvisoft.exception;

public class InsufficientLeaveBalanceException extends Exception {
   public  InsufficientLeaveBalanceException(){
       super("Leave Balance Insufficient");
   }
}
