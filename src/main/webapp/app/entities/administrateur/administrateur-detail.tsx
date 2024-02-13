import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './administrateur.reducer';

export const AdministrateurDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const administrateurEntity = useAppSelector(state => state.administrateur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="administrateurDetailsHeading">
          <Translate contentKey="isimmManagerApp.administrateur.detail.title">Administrateur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{administrateurEntity.id}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="isimmManagerApp.administrateur.prenom">Prenom</Translate>
            </span>
          </dt>
          <dd>{administrateurEntity.prenom}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="isimmManagerApp.administrateur.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{administrateurEntity.nom}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="isimmManagerApp.administrateur.email">Email</Translate>
            </span>
          </dt>
          <dd>{administrateurEntity.email}</dd>
          <dt>
            <span id="matricule">
              <Translate contentKey="isimmManagerApp.administrateur.matricule">Matricule</Translate>
            </span>
          </dt>
          <dd>{administrateurEntity.matricule}</dd>
          <dt>
            <span id="titre">
              <Translate contentKey="isimmManagerApp.administrateur.titre">Titre</Translate>
            </span>
          </dt>
          <dd>{administrateurEntity.titre}</dd>
          <dt>
            <span id="numTel">
              <Translate contentKey="isimmManagerApp.administrateur.numTel">Num Tel</Translate>
            </span>
          </dt>
          <dd>{administrateurEntity.numTel}</dd>
          <dt>
            <span id="dateDeNaissance">
              <Translate contentKey="isimmManagerApp.administrateur.dateDeNaissance">Date De Naissance</Translate>
            </span>
          </dt>
          <dd>
            {administrateurEntity.dateDeNaissance ? (
              <TextFormat value={administrateurEntity.dateDeNaissance} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="photoDeProfile">
              <Translate contentKey="isimmManagerApp.administrateur.photoDeProfile">Photo De Profile</Translate>
            </span>
          </dt>
          <dd>
            {administrateurEntity.photoDeProfile ? (
              <div>
                {administrateurEntity.photoDeProfileContentType ? (
                  <a onClick={openFile(administrateurEntity.photoDeProfileContentType, administrateurEntity.photoDeProfile)}>
                    <img
                      src={`data:${administrateurEntity.photoDeProfileContentType};base64,${administrateurEntity.photoDeProfile}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {administrateurEntity.photoDeProfileContentType}, {byteSize(administrateurEntity.photoDeProfile)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/administrateur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/administrateur/${administrateurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AdministrateurDetail;
