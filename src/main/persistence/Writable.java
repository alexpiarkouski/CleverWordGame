package persistence;

import org.json.JSONObject;

// Modified from WorkRoomApp
// Writable interface
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}