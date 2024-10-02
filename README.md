# LibraryManagement

Ứng dụng **LibraryManagement** được phát triển bởi đội ngũ **CSAG** nhằm hỗ trợ quản lý thư viện một cách hiệu quả và
tiện lợi.

## Tính năng chính

- **Quản lý sách**: Thêm, sửa, xóa và tìm kiếm thông tin sách.

# Hướng dẫn clone và commit code

**Bước 1:** Clone git `git clone https://github.com/dat13298/EProject2_ProSmiles.git`

**Bước 2:** Tạo branch

- Tạo branch mới: `git checkout -b <tên branch>`
- Update branch: `git push -u origin <tên branch>`

### Lưu ý:

- Phải tạo check branch trước khi pull hoặc push code `git checkout <tên branch>`
- Ký hiệu trong tên `fx:id`:
    - `txt`: `TextField`
    - `pwd`: `PasswordField`
    - `cb`: `CheckBox`
    - `hl`: `HyperLink`
    - `btn`: `Button`
    - `lbl`: `Label`

# Git Flow trong Project

## Quy trình khi bắt đầu code:

**Lưu Trữ Các Thay Đổi Hiện Tại:**

`git stash --include-untracked`

Giải Thích: Lệnh này lưu trữ tạm thời tất cả các thay đổi hiện tại (bao gồm cả các tệp chưa được theo dõi) vào một
stash, giúp bạn quay lại trạng thái sạch của nhánh mà không làm mất các thay đổi đó.

**Chuyển Đến Nhánh Chính:**

`git checkout main`

Giải Thích: Chuyển từ nhánh hiện tại sang nhánh main.

**Cập Nhật Nhánh Chính:**

`git pull`

Giải Thích: Tải về và hợp nhất các thay đổi mới nhất từ remote repository vào nhánh main của bạn.

**Chuyển Đến Nhánh Làm Việc:**

`git checkout <tên nhánh đang làm việc>`

Giải Thích: Chuyển sang nhánh làm việc <tên nhánh đang làm việc>.

**Rebase Nhánh Làm Việc Trên Nhánh Chính:**

`git rebase main`

Giải Thích: Lấy tất cả các thay đổi từ nhánh main và áp dụng chúng trước các commit hiện tại trên nhánh <tên nhánh đang
làm việc>, giúp nhánh <tên nhánh đang làm việc> luôn cập nhật với những thay đổi mới nhất từ main.

**Áp Dụng Lại Các Thay Đổi Đã Lưu Trữ:**

`git stash pop`

Giải Thích: Khôi phục các thay đổi từ stash vào nhánh hiện tại (nhánh br_a).

## Quy Trình Khi Commit

**Thêm Các Thay Đổi Vào Stage:**

`git add .`

Giải Thích: Thêm tất cả các thay đổi (bao gồm tệp mới, tệp đã chỉnh sửa và tệp đã xóa) vào khu vực stage, chuẩn bị cho
việc commit.

**Tạo Commit Với message:**

`git commit -m "<Nội dung commit>"`

Giải Thích: Tạo một commit mới với thông điệp mô tả nội dung của các thay đổi. Thông điệp này giúp bạn và người khác
hiểu rõ những gì đã thay đổi trong commit này.

**Push Các Thay Đổi Lên Remote Repository:**

`git push origin <tên nhánh đang làm việc>`

Giải Thích: Đẩy các commit từ nhánh <tên nhánh đang làm việc> lên remote repository trên nhánh br_a.

## Tóm Tắt Quy Trình

1. Lưu trữ các thay đổi hiện tại bằng `git stash` để có một trạng thái sạch.
2. Chuyển sang nhánh chính `git chechout main` và cập nhật nó bằng `git pull`.
3. Chuyển lại nhánh làm việc `<tên nhánh>` và thực hiện rebase với `main` để đảm bảo nhánh của bạn cập nhật với những
   thay đổi mới nhất.
4. Áp dụng lại các thay đổi đã lưu trữ bằng `git stash pop`.
5. Thêm các thay đổi vào stage bằng `git add .`, tạo commit mới bằng `git commit -m <nội dung commit>` và push chúng lên
   remote repository với `git push origin <tên nhánh>`.

# Hướng dẫn folder code

## java folder

- Connectivity: Chứa các file `connection DB`.
- Controller: Chứa các `Controller` tương tác với giao diện khi có `action` từ `User`.
- Global: Chứa các file như `format`, `validate form`, `AppProperties`.
- IGeneric: `Interface` cho các class liên quan đến việc tương tác với `DB`.
- Model: Các thực thể của dự án như `Entity` và `ENUM`.
- Repository: Chứa các Class `DAO` của thực thể `Implement IGeneric` để thực hiện `CRUD` hoặc `đọc ghi file`.
- Service: Các Class liên quan đến xử lý logic như `Authenticate,vv...`

## Luồng xử lý theo quy trình

- *Controller* -> *Service* -> *Repository* (nếu `cần` xử lý logic để tạo ra dữ liệu cuối).
- *Controller* -> *Repository* (nếu `không cần` xử lý logic để tạo ra dữ liệu cuối).

## resources folder

- Media: Lưu trữ file hình ảnh
- Style: _CSS_.
- View: File _fxml_

# Hướng dẫn khi commit message

Sử dụng cú
pháp: `<loại commit>: <mô tả ngắn gọn> [Mô tả chi tiết (nếu cần thiết)] [Tham chiếu đến ticket hoặc issue (nếu có)]`

## Cú pháp commit

- **feat**: Thêm một tính năng mới. Ví dụ: `feat: Thêm chức năng tìm kiếm sách`
- **fix**: Sửa một lỗi. Ví dụ: `fix: Sửa lỗi không hiển thị danh sách độc giả`
- **docs**: Thay đổi về tài liệu (không liên quan đến mã nguồn). Ví dụ: `docs: Cập nhật hướng dẫn cài đặt trong README`
- **style**: Thay đổi về định dạng, dấu cách, thiếu dấu chấm phẩy, v.v., không ảnh hưởng đến mã nguồn. Ví
  dụ: `style: Định dạng lại mã nguồn theo chuẩn`
- **refactor**: Thay đổi mã nguồn mà không sửa lỗi hay thêm tính năng mới. Ví dụ: `refactor: Tối ưu hóa hàm xử lý dữ liệu`
- **test**: Thêm hoặc sửa bài kiểm tra. Ví dụ: `test: Thêm unit test cho module mượn trả`
- **chore**: Cập nhật công cụ xây dựng hoặc thư viện bên ngoài. Ví dụ: `chore: Nâng cấp phiên bản Maven`