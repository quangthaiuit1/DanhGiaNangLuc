package trong.lixco.com.util;

import java.util.ArrayList;
import java.util.List;

import trong.lixco.com.jpa.dto.BaoCaoTongHopDTO;
import trong.lixco.com.jpa.dto.BaoCaoTuyChonDTO;
import trong.lixco.com.jpa.entity.KyDanhGia;

public class ToObjectFromClass {
	public static List<Object[]> mauexcelnangluc() {
		List<Object[]> results = new ArrayList<Object[]>();

		Object[] title = { "Mã năng lực", "Nội dung" };
		results.add(title);
		Object[] data = { "A008", "Sáng tạo, sáng kiến cải tiến" };
		Object[] data1 = { "A009", "Trung thành, gắn bó" };
		Object[] data2 = { "A010", "Sự cảm thông" };
		Object[] data3 = { "B001", "Tầm nhìn" };
		Object[] data4 = { "B002", "Hoạch định và triển khai chiến lược" };
		Object[] data5 = { "B003", "Truyền cảm hứng" };
		results.add(data);
		results.add(data1);
		results.add(data2);
		results.add(data3);
		results.add(data4);
		results.add(data5);
		return results;

	}

	public static List<Object[]> baocaotuychon(List<BaoCaoTuyChonDTO> baoCaoTuyChonDTOs) {
		List<Object[]> results = new ArrayList<Object[]>();

		Object[] title = { "kyDanhGia", "maphongban", "phongban", "manv", "tennv", "ketqua", "machucdanh",
				"tenchucdanh" };
		results.add(title);
		for (int i = 0; i < baoCaoTuyChonDTOs.size(); i++) {
			Object[] data = { baoCaoTuyChonDTOs.get(i).getKyDanhGia().getTenkydanhgia(), baoCaoTuyChonDTOs.get(i).getMaphongban(),
					baoCaoTuyChonDTOs.get(i).getPhongban(), baoCaoTuyChonDTOs.get(i).getManv(),
					baoCaoTuyChonDTOs.get(i).getTennv(), baoCaoTuyChonDTOs.get(i).getKetqua(),
					baoCaoTuyChonDTOs.get(i).getMachucdanh(), baoCaoTuyChonDTOs.get(i).getChucdanh() };
			results.add(data);
		}
		return results;

	}

}
