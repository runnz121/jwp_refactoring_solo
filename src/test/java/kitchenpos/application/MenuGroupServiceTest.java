package kitchenpos.application;

import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그륩이 저장되는지 확인하는 테스트")
    void createTest() {

        MenuGroup sample = makeMenuGroup();

        willDoNothing().given(menuGroupService).create(sample);



    }

    @DisplayName("메뉴그룹 리스트를 불러오는지 확인하는 테스트")
    void listTest() {

        List<MenuGroup> lists = makeMenuGroupList();

    }

    private MenuGroup makeMenuGroup() {
        MenuGroup sample = new MenuGroup();
        sample.setId(1L);
        sample.setName("test");
        return sample;
    }

    private List<MenuGroup> makeMenuGroupList() {
        List<MenuGroup> lists = new ArrayList<>();

        MenuGroup sample1 = new MenuGroup();
        sample1.setId(1L);
        sample1.setName("test1");

        MenuGroup sample2 = new MenuGroup();
        sample2.setId(2L);
        sample2.setName("test2");

        lists.add(sample1);
        lists.add(sample2);

        return lists;
    }
}