package tmpsandbox.microarch.ddd.delivery.core.ports;

import libs.errs.Error;
import libs.errs.Result;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;

public interface GeoClient {
    Result<Location, Error> getLocation(String street);
}
