package net.jorhlok.tracker0_1.jorhtrackpack;

/**
 * Digital Envelope performs the function of an ADSR envelope but lend themselves to low resolution values
 * @author jorh
 */
public class DigitalEnvelope {
    protected int[] Envelope;
    protected int ValueMax;
    protected int ValueMin;
    protected int ValueNull;
    protected byte TimeScaler;
    protected short LoopPoint; //the first point in the loop or no loop if it's the same as the sustain point
    protected short SustainPoint; //also acts as the last value in the loop
    protected short EndPoint;
    
    protected short CurrentPoint;
    protected byte CurrentCounter;
    
    public DigitalEnvelope() {
        Envelope = new int[1];
        Envelope[0] = 0;
        ValueMax = 1;
        ValueMin = ValueNull = 0;
        TimeScaler = 1;
        LoopPoint = SustainPoint = EndPoint = 0;
        CurrentPoint = CurrentCounter = 0;
    }
    
    public DigitalEnvelope(int max, int min, int nul) {
        this();
        ValueMax = max;
        ValueMin = min;
        Envelope[0] = ValueNull = nul;
    }
    
    public void step(int steps) {
        for (int i=0; i<steps; ++i)
            step();
    }
    
    public void step() {
        ++CurrentCounter;
        if (CurrentCounter >= TimeScaler) {
            CurrentCounter = 0;
            if (CurrentPoint == SustainPoint)
                CurrentPoint = LoopPoint;
            else if (CurrentPoint != EndPoint)
                ++CurrentPoint;
        }
    }
    
    public int getValue() {
        if (CurrentPoint >= Envelope.length || CurrentPoint < 0)
            return ValueNull;
        return Envelope[CurrentPoint];
    }
    
    public int[] getEnvelope() {
        return Envelope.clone();
    }

    public void setEnvelope(int[] Envelope) {
        this.Envelope = new int[Envelope.length];
        for (int i=0; i<this.Envelope.length; ++i)
            this.Envelope[i] = Math.min(ValueMax, Math.max(ValueMin, Envelope[i]));
    }
    
    public void setValueBounds(int max, int min, int nul) {
       ValueMax = max;
       ValueMin = min;
       ValueNull = nul;
       setEnvelope(getEnvelope()); //re-check envelope
    }

    public int getValueMax() {
        return ValueMax;
    }

    public void setValueMax(int ValueMax) {
        this.ValueMax = ValueMax;
        setEnvelope(getEnvelope()); //re-check envelope
    }

    public int getValueMin() {
        return ValueMin;
    }

    public void setValueMin(int ValueMin) {
        this.ValueMin = ValueMin;
        setEnvelope(getEnvelope()); //re-check envelope
    }

    public int getValueNull() {
        return ValueNull;
    }

    public void setValueNull(int ValueNull) {
        this.ValueNull = ValueNull;
    }

    public byte getTimeScaler() {
        return TimeScaler;
    }

    public void setTimeScaler(int TimeScaler) {
        this.TimeScaler = (byte)TimeScaler;
    }

    public short getLoopPoint() {
        return LoopPoint;
    }

    public void setLoopPoint(int LoopPoint) {
        this.LoopPoint = (short)LoopPoint;
    }

    public short getSustainPoint() {
        return SustainPoint;
    }

    public void setSustainPoint(int SustainPoint) {
        this.SustainPoint = (short)SustainPoint;
    }

    public short getEndPoint() {
        return EndPoint;
    }

    public void setEndPoint(int EndPoint) {
        this.EndPoint = (short)EndPoint;
    }

    public short getCurrentPoint() {
        return CurrentPoint;
    }

    public void setCurrentPoint(int CurrentPoint) {
        this.CurrentPoint = (short)CurrentPoint;
    }

    public byte getCurrentCounter() {
        return CurrentCounter;
    }

    public void setCurrentCounter(int CurrentCounter) {
        this.CurrentCounter = (byte)CurrentCounter;
    }
    
    @Override
    public String toString() {
        String ret = "{\t" + Integer.toHexString(ValueMax) + ", " + Integer.toHexString(ValueMin) + ", " + Integer.toHexString(ValueNull) + ",\n\t";
        ret += Integer.toHexString(TimeScaler) + ", " + Integer.toHexString(LoopPoint) + ", " + Integer.toHexString(SustainPoint) + ", " + Integer.toHexString(EndPoint) + ",\n\t";
        if (Envelope != null && Envelope.length > 0) {
            ret += Integer.toHexString(Envelope[0]);
            for (int i=1; i<Envelope.length; ++i)
                ret += ", " + Integer.toHexString(Envelope[i]);
        }
        else ret += "0";
        return ret + "\n}";
    }
}
