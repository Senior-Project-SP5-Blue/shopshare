import ListItemStatus from './ListItemStatus';

// String id, String name, String status, UUID createdBy, Boolean locked, LocalDateTime createdOn
type ListItemDto = {
  id: string;
  name: string;
  status: ListItemStatus;
  createdBy: string;
  locked: boolean;
  createdOn: string;
};

export default ListItemDto;
