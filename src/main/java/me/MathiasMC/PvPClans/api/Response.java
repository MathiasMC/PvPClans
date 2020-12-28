package me.MathiasMC.PvPClans.api;

public class Response {

    public enum Withdraw {
        ENOUGH, SUCCESS, CANCELLED
    }

    public enum Create {
        SUCCESS, NAME, EXISTS, ENOUGH
    }

    public enum Invite {
        SUCCESS, LIMIT
    }

    public enum Rename {
        SUCCESS, NAME, EXISTS
    }

    public enum WithdrawType {
        CLAN, OWN
    }

    public enum Accept {
        SUCCESS, LIMIT
    }
}
