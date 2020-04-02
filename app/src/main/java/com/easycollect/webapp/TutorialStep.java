package com.easycollect.webapp;

public class TutorialStep {
    private boolean _finalStep;
    public boolean _isActive;

    public TutorialStep(boolean finalStep){
        _finalStep = finalStep;
    }

    public boolean isActive(){
        return _isActive;
    }

    public boolean isFinalStep(){
        return _finalStep;
    }
}
