import ListItemDto from "../listitem/ListItemDto";

type ShoppingListDto = {
  id: string;
  name: string;
  modifiedOn: string;
  items: ListItemDto[];
};

export default ShoppingListDto;
