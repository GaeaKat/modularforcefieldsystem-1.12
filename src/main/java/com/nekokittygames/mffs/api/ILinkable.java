package com.nekokittygames.mffs.api;

import java.util.UUID;

/**
 * Created by Katrina on 21/04/2016.
 */
public interface ILinkable {

    /**
     * Gets the network ID that this block is on
     * @return networkID
     */
    UUID GetNetworkId();

    /**
     * called when block is linked to a network
     * @param network network id
     */
    void LinkToNetwork(UUID network);

    /**
     * called if block is delinked from a network
     * @param network
     */
    void DelinkFromNetwork(UUID network);
}
