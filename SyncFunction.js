function(doc, oldDoc){

  // ################################################################
  // "type" property is mandaroty and accessible for all the function
  // ################################################################
  var type = oldDoc ? oldDoc.type : doc.type;
  if (!type) {
    throw({forbidden : "Document must have a type."});
  }

  // TYPE
  var COMPANY = "company";
  var DEVICE = "device";
  var MISSION = "mission";
  var MISSION_STATUS = "mission_status";
  var TRACK = "track";
  var METADATA = "metadata";

  // ACTION
  var CREATING = "creating";
  var UPDATING = "updating";
  var DELETING = "deleting";

  // #############
  // MAIN FUNCTION
  // #############
  var action = getAction(doc, oldDoc);
  switch(type) {
    case COMPANY:
      company(doc, oldDoc, action);
      break;
    case DEVICE:
      device(doc, oldDoc, action);
      break;
    case MISSION:
      mission(doc, oldDoc, action);
      break;
    case MISSION_STATUS:
      // TODO mission_status(doc, oldDoc, action);
      break;
    case TRACK:
      // TODO track(doc, oldDoc, action);
      break;
    case METADATA:
      // TODO metadata(doc, oldDoc, action);
      break;
    default:
  }

  // ###############
  // COMPANY MANAGER
  // ###############
  function company(doc, oldDoc, action){
    var owner = checkOwner(doc, oldDoc);
    requireUser(owner);
    switch(action) {
      case CREATING:
        break;
      case UPDATING:
        break;
      case DELETING:
        break;
      default:
    }
  }

  // ##############
  // DEVICE MANAGER
  // ##############
  function device(doc, oldDoc, action){  
    var owner = checkOwner(doc, oldDoc);
    // FIXME We can't check owner, because in reality device will be create by
    // another user.
    requireUser(owner);

    var channel = makeDeviceChannel(doc, oldDoc, owner, type);
    switch(action) {
      case CREATING:
        access(owner, channel);
        break;
      case UPDATING:
        break;
      case DELETING:
        break;
      default:
    }
  }

  // ###############
  // MISSION MANAGER
  // ###############
  function mission(doc, oldDoc, action){
    var owner = checkOwner(doc, oldDoc);
    var delivery_date = checkDeliveryDate(doc, oldDoc);
    var channel = makeMissionChannel(doc, oldDoc, owner, type, delivery_date);
    checkDevice(doc, oldDoc);
    requireUser(owner);
    switch(action) {
      case CREATING:
        access(owner, channel);
        break;
      case UPDATING:
        break;
      case DELETING:
        break;
      default:
    }
  }

  // #########################################################
  // #########################################################
  // ##                                                     ##
  // ##                 Function definition                 ##
  // ##                                                     ##
  // #########################################################
  // #########################################################

  // #################################
  // Check Document Field
  // #################################
  function checkOwner(doc, oldDoc) {
    // Make sure that the owner propery exists:
    var owner = oldDoc ? oldDoc.owner : doc.owner;
    if (!owner) {
      throw({forbidden : "Document must have a owner."});
    }

    // Make sure that only the owner of the list can update the list:
    if (doc.owner && owner != doc.owner) {
      throw({forbidden : "Cannot change owner for missions."});
    }
    return owner;
  }

  function checkDeliveryDate(doc, oldDoc) {
    // Make sure that the checkDeliveryDate propery exists:
    var delivery_date = oldDoc ? oldDoc.delivery_date : doc.delivery_date;
    if (!delivery_date) {
      throw({forbidden : "Document must have a delivery_date."});
    }
    return delivery_date;
  }

  function checkDevice(doc, oldDoc) {
    // Make sure that the checkDeliveryDate propery exists:
    var device = oldDoc ? oldDoc.device : doc.device;
    if (!device) {
      throw({forbidden : "Document must have a device."});
    }
    return device;
  }

  // ###############
  // Channel Factory
  // ###############
  function makeDeviceChannel(doc, oldDoc, owner, type) {
    var owner_channel = type + ":" + owner;
    channel(owner_channel);
    return owner_channel;
  }

  function makeMissionChannel(doc, oldDoc, owner, type, delivery_date) {
    // Date format mmMMyyyy for channel
    var timestamp = Date.parse(delivery_date);
    if(isNaN(timestamp))
      throw({forbidden : "Document must have a delivery_date ISO8601 valid format."});
    var date = new Date(timestamp);
    var channel_date = "" + ("0" + date.getDate()).slice(-2)
                  + ("0" + (date.getMonth())).slice(-2)
                  + date.getFullYear();

    doc.date_test = channel_date;

    var owner_channel = type + ":" +owner + ":" + channel_date;
    channel(owner_channel);
    return owner_channel;
  }

  // #############
  // Action Helper
  // #############
  function getAction(doc, oldDoc) {
    var action = "creating";
    if(doc && !oldDoc) {
      action = "creating";
    }
    else if(doc && oldDoc) {
      action = "updating";
      if(doc._deleted === true && oldDoc._deleted === false)
        action = "deleting";
    }
    else {
      action = "unknow";
    }
    return action;
  }
}

