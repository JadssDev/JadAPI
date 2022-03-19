package dev.jadss.jadapi.management.labymod.features;

import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.UUID;

/**
 * Update the <b>Party Info Status</b> of LabyMod.
 */
public class UpdatePartyInfoPacket extends LabyModPacket {

    public boolean hasParty;
    public UUID partyId;
    public int party_size;
    public int party_max;

    public UpdatePartyInfoPacket(boolean hasParty, UUID partyId, int party_size, int party_max) {
        this.hasParty = hasParty;
        this.partyId = partyId;
        this.party_size = party_size;
        this.party_max = party_max;
    }

    @Override
    public String getMessageKey() {
        return "discord_rpc";
    }

    @Override
    public void parse(JsonObject object) {
        this.hasParty = object.get("hasParty").getAsBoolean();
        this.partyId = UUID.fromString(object.get("partyId").getAsString());
        this.party_size = object.get("party_size").getAsInt();
        this.party_max = object.get("party_max").getAsInt();
    }

    @Override
    public String buildString() {
        return g.toJson(this);
    }

    @Override
    public LabyModPacket copy() {
        return new UpdatePartyInfoPacket(hasParty, partyId, party_size, party_max);
    }
}
