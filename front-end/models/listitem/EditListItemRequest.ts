import ListItemStatus from "./ListItemStatus";

type EditListItemRequest = {
  name: string;
  status: ListItemStatus;
  locked: boolean;
};

export default EditListItemRequest;
