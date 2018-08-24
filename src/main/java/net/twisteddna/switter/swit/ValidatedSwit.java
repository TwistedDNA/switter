package net.twisteddna.switter.swit;

class ValidatedSwit {
    private final Swit swit;
    private final boolean valid;
    private String errorMessage = "";

    public ValidatedSwit(Swit swit) {
        this.swit = swit;
        this.valid = this.validate();
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    private boolean validate() {
        if (swit == null) {
            errorMessage = "Missing swit object";
            return false;
        }
        if (swit.getText() == null) {
            errorMessage = "Swit text cannot be null. Use empty string instead.";
            return false;
        }
        if (swit.getText().length() > Swit.LENGTH_LIMIT) {
            errorMessage = "Swit length excided limit of " + Swit.LENGTH_LIMIT + " symbols.";
            return false;
        }
        if (swit.getAuthorUsername() == null || swit.getAuthorUsername().isEmpty()) {
            errorMessage = "Swit author username is missing.";
            return false;
        }
        return true;
    }
}
