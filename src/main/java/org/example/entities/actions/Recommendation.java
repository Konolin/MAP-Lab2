package org.example.entities.actions;

import org.example.entities.users.User;

public class Recommendation {
    private Integer id;
    private User toUser;
    private User fromUser;
    private String reason;

    public Recommendation(Integer id, User toUser, User fromUser, String reason) {
        this.id = id;
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.reason = reason;
    }
}
