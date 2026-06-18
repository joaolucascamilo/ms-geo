# ms-geo

Microsserviço de geolocalização do sistema de infraestrutura urbana (TCC). Responsável por armazenar e consultar ocorrências no mapa, gerar dados para mapa de calor e resolver endereços/CEPs.

## Visão geral

```
ms-ocorrencias ──POST /api/geo/ocorrencias──▶ ms-geo ──▶ PostgreSQL + PostGIS
                                                  │
                                          ┌───────┴────────┐
                                     Nominatim          ViaCEP
                                  (geocodificação)   (busca por CEP)
```

Quando uma ocorrência chega, o serviço resolve as coordenadas geográficas por um de dois caminhos:
- **Endereço textual** (`rua` + `cidade`) → geocodificação via Nominatim (OpenStreetMap)
- **Coordenadas diretas** (`latitude` + `longitude`) → usadas diretamente

A localização é persistida como `geometry(Point, 4326)` no PostGIS, o que permite consultas espaciais nativas.

## Requisitos

- Java 17
- Maven 3.9+
- PostgreSQL com extensão **PostGIS** habilitada

## Configuração do banco

```sql
CREATE DATABASE infra_urbana_geolocalizacao;
\c infra_urbana_geolocalizacao
CREATE EXTENSION postgis;
```

As credenciais padrão estão em `src/main/resources/application.yml`:

| Parâmetro | Valor padrão |
|---|---|
| Host | `localhost:5432` |
| Database | `infra_urbana_geolocalizacao` |
| Usuário | `admin` |
| Senha | `password123` |

O schema é gerenciado automaticamente pelo Hibernate (`ddl-auto: update`).

## Executando

```bash
./mvnw spring-boot:run
```

A aplicação sobe na porta **8084**.

## Build

```bash
./mvnw clean package
```

O JAR gerado fica em `target/ms-geo-0.0.1-SNAPSHOT.jar`.

## Testes

```bash
# Todos os testes
./mvnw test

# Classe específica
./mvnw test -Dtest=MsGeoApplicationTests
```

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/geo/ocorrencias` | Registra uma ocorrência no mapa (chamado pelo ms-ocorrencias) |
| `GET` | `/api/geo/ocorrencias/raio` | Busca ocorrências num raio (params: `lat`, `lng`, `raioMetros`) |
| `GET` | `/api/geo/mapa-calor` | Retorna agrupamento por categoria para mapa de calor |
| `GET` | `/api/geo/cep/{cep}` | Resolve um CEP em endereço via ViaCEP |

### Exemplo — registrar ocorrência por endereço

```json
POST /api/geo/ocorrencias
{
  "id": 1,
  "categoria": "BURACO",
  "status": "ABERTO",
  "quantidadeDenuncias": 3,
  "dataCriacao": "2025-06-17T10:00:00",
  "rua": "Rua das Flores",
  "bairro": "Centro",
  "cidade": "São Paulo",
  "estado": "SP",
  "pais": "Brasil"
}
```

### Exemplo — registrar ocorrência por coordenadas

```json
POST /api/geo/ocorrencias
{
  "id": 2,
  "categoria": "ILUMINACAO",
  "status": "ABERTO",
  "quantidadeDenuncias": 1,
  "dataCriacao": "2025-06-17T10:00:00",
  "latitude": -23.5505,
  "longitude": -46.6333
}
```

### Exemplo — buscar por raio

```
GET /api/geo/ocorrencias/raio?lat=-23.5505&lng=-46.6333&raioMetros=1000
```

## Dependências principais

| Dependência | Finalidade |
|---|---|
| `hibernate-spatial` | Mapeamento de tipos geográficos JTS ↔ PostGIS |
| `jts-core` | Criação de objetos `Point`/`Coordinate` em Java |
| `spring-cloud-starter-openfeign` | Clientes HTTP declarativos (Nominatim, ViaCEP, ms-ocorrencias) |
| `spring-boot-starter-actuator` | Healthcheck em `/actuator/health` |

## Integração com outros microsserviços

O endereço do `ms-ocorrencias` é configurável via `application.yml`:

```yaml
ms-ocorrencias:
  url: http://localhost:8081
```
