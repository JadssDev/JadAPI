package dev.jadss.jadapi.utils;

public class DebugOptions {

    private boolean reflectionDebug;
    private boolean eventsDebug;
    private boolean channelDebug;
    private boolean quickEventsDebug;
    private boolean packetHooksDebug;
    private boolean miscDebug;

    public DebugOptions() { }

    public boolean doReflectionDebug() { return reflectionDebug; }
    public void setReflectionDebug(boolean reflectionDebug) { this.reflectionDebug = reflectionDebug; }

    public boolean doEventsDebug() { return eventsDebug; }
    public void setEventsDebug(boolean eventsDebug) { this.eventsDebug = eventsDebug; }

    public boolean doChannelDebug() { return channelDebug; }
    public void setChannelDebug(boolean channelDebug) { this.channelDebug = channelDebug; }

    public boolean doMiscDebug() { return miscDebug; }
    public void setMiscDebug(boolean miscDebug) { this.miscDebug = miscDebug; }

    public boolean doQuickEventsDebug() { return quickEventsDebug; }
    public void setQuickEventsDebug(boolean quickEventsDebug) { this.quickEventsDebug = quickEventsDebug; }

    public boolean doPacketHooksDebug() { return packetHooksDebug; }
    public void setPacketHooksDebug(boolean packetHooksDebug) { this.packetHooksDebug = packetHooksDebug; }
}
