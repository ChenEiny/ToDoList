//Chen Einy 209533785
//Eli Shulman 316040120

package com.hit.dao;

public class TaskAlreadyExistsException extends Exception {
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
