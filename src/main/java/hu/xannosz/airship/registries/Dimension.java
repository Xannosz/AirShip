package hu.xannosz.airship.registries;

import hu.xannosz.airship.shipmodels.ShipModel;

public record Dimension(int key, String name, ShipModel model, int referenceY) {
}
