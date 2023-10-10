package com.sisco.playground.coachmark

interface SequenceListener{
    fun onNextItem(coachMark : CoachMarkOverlay, coachMarkSequence : CoachMarkSequence){
        coachMarkSequence.setNextView()
    }
}