package t1internship.placesservice.api.mapper;

import t1internship.placesservice.api.dto.request.*;
import t1internship.placesservice.api.dto.response.*;
import t1internship.placesservice.data.entity.Spaces.*;

public class SpaceMapper {

    public static Space toEntity(SpaceRequest request) {
        if (request instanceof MeetingRoomRequest) {
            MeetingRoomRequest req = (MeetingRoomRequest) request;
            MeetingRoom meetingRoom = new MeetingRoom();
            meetingRoom.setName(req.getName());
            meetingRoom.setSeatingCapacity(req.getSeatingCapacity());
            meetingRoom.setHasProjector(req.getHasProjector());
            meetingRoom.setHasDrawingBoard(req.getHasDrawingBoard());
            return meetingRoom;
        } else if (request instanceof KitchenRequest) {
            KitchenRequest req = (KitchenRequest) request;
            Kitchen kitchen = new Kitchen();
            kitchen.setName(req.getName());
            kitchen.setSeatingCapacity(req.getSeatingCapacity());
            kitchen.setHasCoffeeMachine(req.getHasCoffeeMachine());
            kitchen.setHasMicrowave(req.getHasMicrowave());
            kitchen.setHasFridge(req.getHasFridge());
            return kitchen;
        } else if (request instanceof ChillSpaceRequest) {
            ChillSpaceRequest req = (ChillSpaceRequest) request;
            ChillSpace chillSpace = new ChillSpace();
            chillSpace.setName(req.getName());
            chillSpace.setSeatingCapacity(req.getSeatingCapacity());
            chillSpace.setNumberOfSockets(req.getNumberOfSockets());
            return chillSpace;
        } else if (request instanceof WorkSpaceRequest) {
            WorkSpaceRequest req = (WorkSpaceRequest) request;
            WorkSpace workSpace = new WorkSpace();
            workSpace.setName(req.getName());
            workSpace.setSeatingCapacity(req.getSeatingCapacity());
            workSpace.setHasComputer(req.getHasComputer());
            workSpace.setNumberOfSockets(req.getNumberOfSockets());
            workSpace.setNumberOfMonitors(req.getNumberOfMonitors());

            return workSpace;
        }
        throw new IllegalArgumentException("Not valid type of request");
    }

    public static SpaceResponse toResponse(Space space) {
        if (space instanceof MeetingRoom) {
            Long id = space.getId();
            MeetingRoom room = (MeetingRoom) space;
            MeetingRoomResponse response = new MeetingRoomResponse();
            response.setId(id);
            response.setName(room.getName());
            response.setSeatingCapacity(room.getSeatingCapacity());
            response.setFloorId(room.getFloor().getId());
            response.setHasProjector(room.getHasProjector());
            response.setHasDrawingBoard(room.getHasDrawingBoard());
            response.setSpaceType("MEETING_ROOM");
            return response;
        } else if (space instanceof Kitchen) {
            Long id = space.getId();
            Kitchen kitchen = (Kitchen) space;
            KitchenResponse response = new KitchenResponse();
            response.setId(id);
            response.setName(kitchen.getName());
            response.setSeatingCapacity(kitchen.getSeatingCapacity());
            response.setFloorId(kitchen.getFloor().getId());
            response.setHasCoffeeMachine(kitchen.getHasCoffeeMachine());
            response.setHasMicrowave(kitchen.getHasMicrowave());
            response.setHasFridge(kitchen.getHasFridge());
            response.setSpaceType("KITCHEN");
            return response;
        } else if (space instanceof ChillSpace) {
            Long id = space.getId();
            ChillSpace chill = (ChillSpace) space;
            ChillSpaceResponse response = new ChillSpaceResponse();
            response.setId(id);
            response.setName(chill.getName());
            response.setSeatingCapacity(chill.getSeatingCapacity());
            response.setFloorId(chill.getFloor().getId());
            response.setNumberOfSockets(chill.getNumberOfSockets());
            response.setSpaceType("CHILL_SPACE");
            return response;
        } else if (space instanceof WorkSpace) {
            Long id = space.getId();
            WorkSpace work = (WorkSpace) space;
            WorkSpaceResponse response = new WorkSpaceResponse();
            response.setId(id);
            response.setName(work.getName());
            response.setSeatingCapacity(work.getSeatingCapacity());
            response.setFloorId(work.getFloor().getId());
            response.setHasComputer(work.getHasComputer());
            response.setNumberOfSockets(work.getNumberOfSockets());
            response.setNumberOfMonitors(work.getNumberOfMonitors());
            response.setSpaceType("WORK_SPACE");
            return response;
        }
        throw new IllegalArgumentException("Not valid type of request");
    }
    public static Space updateEntity(SpaceRequest request, Space space) {
        if (request instanceof MeetingRoomRequest && space instanceof MeetingRoom) {
            MeetingRoomRequest req = (MeetingRoomRequest) request;
            MeetingRoom meetingRoom = (MeetingRoom) space;
            meetingRoom.setName(req.getName());
            meetingRoom.setSeatingCapacity(req.getSeatingCapacity());
            meetingRoom.setHasProjector(req.getHasProjector());
            meetingRoom.setHasDrawingBoard(req.getHasDrawingBoard());
            return meetingRoom;
        } else if (request instanceof KitchenRequest && space instanceof Kitchen) {
            KitchenRequest req = (KitchenRequest) request;
            Kitchen kitchen = (Kitchen) space;
            kitchen.setName(req.getName());
            kitchen.setSeatingCapacity(req.getSeatingCapacity());
            kitchen.setHasCoffeeMachine(req.getHasCoffeeMachine());
            kitchen.setHasMicrowave(req.getHasMicrowave());
            kitchen.setHasFridge(req.getHasFridge());
            return kitchen;
        } else if (request instanceof ChillSpaceRequest && space instanceof ChillSpace) {
            ChillSpaceRequest req = (ChillSpaceRequest) request;
            ChillSpace chillSpace = (ChillSpace) space;
            chillSpace.setName(req.getName());
            chillSpace.setSeatingCapacity(req.getSeatingCapacity());
            chillSpace.setNumberOfSockets(req.getNumberOfSockets());
            return chillSpace;
        } else if (request instanceof WorkSpaceRequest && space instanceof WorkSpace) {
            WorkSpaceRequest req = (WorkSpaceRequest) request;
            WorkSpace workSpace = (WorkSpace) space;
            workSpace.setName(req.getName());
            workSpace.setSeatingCapacity(req.getSeatingCapacity());
            workSpace.setHasComputer(req.getHasComputer());
            workSpace.setNumberOfSockets(req.getNumberOfSockets());
            workSpace.setNumberOfMonitors(req.getNumberOfMonitors());
            return workSpace;
        }
        throw new IllegalArgumentException(
                String.format("Type mismatch: request is %s, but entity is %s",
                        request.getClass().getSimpleName(), space.getClass().getSimpleName()));
    }


}
