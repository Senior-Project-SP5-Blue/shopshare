type ShopperGroupDto = {
  id: string;
  name: string;
  admin: string;
  users: number;
  lists: SlimList[];
};

type SlimList = {
  id: string;
  name: string;
};

export default ShopperGroupDto;
