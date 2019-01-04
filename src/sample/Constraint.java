package sample;

public class Constraint {

    private int acrossIndex, downIndex, acrossLetterNumber, downLetterNumber;

    public Constraint(int acrossIndex, int downIndex, int acrossLetterNumber, int downLetterNumber) {
        this.acrossIndex = acrossIndex;
        this.downIndex = downIndex;
        this.acrossLetterNumber = acrossLetterNumber;
        this.downLetterNumber = downLetterNumber;
    }

    //Getter Setters
    public int getAcrossIndex() { return acrossIndex; }
    public void setAcrossIndex(int acrossIndex) { this.acrossIndex = acrossIndex; }
    public int getDownIndex() { return downIndex; }
    public void setDownIndex(int downIndex) { this.downIndex = downIndex; }
    public int getAcrossLetterNumber() { return acrossLetterNumber; }
    public void setAcrossLetterNumber(int acrossLetterNumber) { this.acrossLetterNumber = acrossLetterNumber; }
    public int getDownLetterNumber() { return downLetterNumber; }
    public void setDownLetterNumber(int downLetterNumber) { this.downLetterNumber = downLetterNumber; }

}