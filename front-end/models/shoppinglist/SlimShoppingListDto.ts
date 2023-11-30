type SlimShoppingListDto = {
  id: string;
  name: string;
  modifiedOn: string;
  completed: number;
  total: number;
  groupId: string;
  color: string | null;
};

export default SlimShoppingListDto;
