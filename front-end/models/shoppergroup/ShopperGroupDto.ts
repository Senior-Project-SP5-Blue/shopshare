import SlimList from '../shoppinglist/SlimList';

type ShopperGroupDto = {
  id: string;
  name: string;
  admin: string;
  users: string[];
  lists: SlimList[];
};

export default ShopperGroupDto;
