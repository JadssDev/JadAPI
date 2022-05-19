package dev.jadss.jadapi.management.labymod.features.discord;

import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.UUID;

/**
 * Update the <b>Party Info Status</b> of LabyMod.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/labymod/discord_rich_presence")
public class UpdatePartyInfoPacket extends LabyModPacket {

    public final boolean hasParty;
    public final UUID partyId;
    public final int party_size;
    public final int party_max;

    public UpdatePartyInfoPacket() {
        this(false, null, 0, 0);
    }

    public UpdatePartyInfoPacket(boolean hasParty, UUID partyId, int party_size, int party_max) {
        this.hasParty = hasParty;
        this.partyId = partyId;
        this.party_size = party_size;
        this.party_max = party_max;
    }

    @Override
    public String getMessageKey() {
        return "DISCORD_RPC";
    }

    public UpdatePartyInfoPacket parse(String json) {
        return internalParse(json, UpdatePartyInfoPacket.class);
    }

    @Override
    public LabyModPacket copy() {
        return new UpdatePartyInfoPacket(hasParty, partyId, party_size, party_max);
    }
}
