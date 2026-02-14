package tmpsandbox.microarch.ddd.delivery.adapters.out.grpc;

import clients.geo.GeoGrpc;
import clients.geo.GeoProto;
import libs.errs.Error;
import libs.errs.Result;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.common.Location;
import tmpsandbox.microarch.ddd.delivery.core.port.GeoClient;

@Service
@Slf4j
public class GeoClientImpl implements GeoClient {

    @GrpcClient("geo")
    private GeoGrpc.GeoBlockingStub geoBlockingStub;

    @Override
    public Result<Location, Error> getLocation(String street) {
        var geoLocationRequest = GeoProto.GetGeolocationRequest.newBuilder()
            .setStreet(street)
            .build();

        log.debug("Build request to geo service: {}", geoLocationRequest);
        var geolocation = geoBlockingStub.getGeolocation(geoLocationRequest);
        log.debug("Response geo service: {}", geolocation);

        return mapToResponse(geolocation);
    }

    private Result<Location, Error> mapToResponse(GeoProto.GetGeolocationReply response) {
        return Location.create(response.getLocation().getX(), response.getLocation().getY());
    }
}
